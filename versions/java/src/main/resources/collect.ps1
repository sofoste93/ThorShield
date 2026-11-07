$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
try {
    if ($check -eq 'defender') {
        $state = Get-MpComputerStatus -ErrorAction Stop
        $value = [ordered]@{
            antivirus_enabled = $state.AntivirusEnabled
            realtime_enabled = $state.RealTimeProtectionEnabled
            running_mode = $state.AMRunningMode
        }
    } elseif ($check -eq 'firewall') {
        $value = [ordered]@{}
        foreach ($profile in @(Get-NetFirewallProfile -PolicyStore ActiveStore -ErrorAction Stop)) {
            $enabled = $null
            if ([string]$profile.Enabled -eq 'True') { $enabled = $true }
            elseif ([string]$profile.Enabled -eq 'False') { $enabled = $false }
            $value[[string]$profile.Name] = $enabled
        }
    } else { throw 'Unsupported collector' }
    @{ status = 'success'; value = $value } | ConvertTo-Json -Compress -Depth 4
} catch {
    $status = 'unavailable'
    if ($_.CategoryInfo.Category -in @('PermissionDenied', 'SecurityError') -or
        $_.Exception -is [System.UnauthorizedAccessException] -or
        $_.Exception.HResult -eq -2147024891) { $status = 'access_denied' }
    # No raw exception text: avoid disclosing local paths or user details.
    @{ status = $status; value = $null } | ConvertTo-Json -Compress
}
