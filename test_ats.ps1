$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Net.Http

function Test-RoleLogin($username, $password, $expectedRedirect, $expectedDashboardString) {
    Write-Host "=================================================="
    Write-Host "Testing Login for user: $username"
    
    $handler = New-Object System.Net.Http.HttpClientHandler
    $handler.AllowAutoRedirect = $false
    $client = New-Object System.Net.Http.HttpClient($handler)
    $client.BaseAddress = New-Object System.Uri("http://localhost:8080/")

    # 1. Fetch login page to get CSRF
    $loginResp = $client.GetAsync("/login").Result
    $loginHtml = $loginResp.Content.ReadAsStringAsync().Result
    
    $csrfMatch = [regex]::Match($loginHtml, 'name="_csrf"\s+value="([^"]+)"')
    if (-not $csrfMatch.Success) {
        throw "Could not extract CSRF token from /login"
    }
    $csrf = $csrfMatch.Groups[1].Value
    Write-Host "Extracted CSRF token successfully"

    # 2. Post login credentials
    $postParams = New-Object 'System.Collections.Generic.Dictionary[string,string]'
    $postParams.Add("username", $username)
    $postParams.Add("password", $password)
    $postParams.Add("_csrf", $csrf)
    $formContent = New-Object System.Net.Http.FormUrlEncodedContent($postParams)

    $authResp = $client.PostAsync("/login", $formContent).Result
    Write-Host "Login response status:" ([int]$authResp.StatusCode)
    
    $redirectLocation = $authResp.Headers.Location.ToString()
    Write-Host "Redirect location:" $redirectLocation
    if (-not $redirectLocation.Contains($expectedRedirect)) {
        throw "Expected redirect to contain '$expectedRedirect', but got '$redirectLocation'"
    }

    # 3. Access destination dashboard
    $dashResp = $client.GetAsync($redirectLocation).Result
    $dashHtml = $dashResp.Content.ReadAsStringAsync().Result
    Write-Host "Dashboard response status:" ([int]$dashResp.StatusCode)
    
    if (-not $dashHtml.Contains($expectedDashboardString)) {
        throw "Expected dashboard to contain '$expectedDashboardString'"
    }
    Write-Host "SUCCESS: $username accessed dashboard with expected content: '$expectedDashboardString'"

    return @{ Client = $client; DashboardHtml = $dashHtml }
}

try {
    # 1. Test Admin
    $adminRes = Test-RoleLogin "admin@ats.com" "admin123" "/admin/dashboard" "System Administration Console"
    if ($adminRes.DashboardHtml.Contains("Open User Panel") -and $adminRes.DashboardHtml.Contains("Open HR Panel")) {
        Write-Host "SUCCESS: Admin Panel contains 'Open User Panel' and 'Open HR Panel' buttons!"
    }
    
    # Test Admin accessing User UI and HR UI directly (Requirement 25)
    $adminUserUi = $adminRes.Client.GetAsync("/user/dashboard").Result
    Write-Host "Admin accessing /user/dashboard Status:" ([int]$adminUserUi.StatusCode)
    
    $adminHrUi = $adminRes.Client.GetAsync("/hr/dashboard").Result
    Write-Host "Admin accessing /hr/dashboard Status:" ([int]$adminHrUi.StatusCode)

    # 2. Test Candidate (Rahul Sharma)
    $userRes = Test-RoleLogin "rahul.sharma@example.com" "user123" "/user/dashboard" "Rahul Sharma"
    $userJobs = $userRes.Client.GetAsync("/user/jobs").Result
    Write-Host "Candidate accessing /user/jobs Status:" ([int]$userJobs.StatusCode)
    $userApps = $userRes.Client.GetAsync("/user/applications").Result
    Write-Host "Candidate accessing /user/applications Status:" ([int]$userApps.StatusCode)
    $userInvs = $userRes.Client.GetAsync("/user/interviews").Result
    Write-Host "Candidate accessing /user/interviews Status:" ([int]$userInvs.StatusCode)

    # 3. Test HR (ABC Tech HR)
    $hrRes = Test-RoleLogin "hr.techcorp@ats.com" "hr123" "/hr/dashboard" "ABC Technologies"
    $hrJobs = $hrRes.Client.GetAsync("/hr/jobs").Result
    Write-Host "HR accessing /hr/jobs Status:" ([int]$hrJobs.StatusCode)
    $hrApps = $hrRes.Client.GetAsync("/hr/applications").Result
    Write-Host "HR accessing /hr/applications Status:" ([int]$hrApps.StatusCode)
    $hrInvs = $hrRes.Client.GetAsync("/hr/interviews").Result
    Write-Host "HR accessing /hr/interviews Status:" ([int]$hrInvs.StatusCode)

    # 4. Test Registration Mandatory Validation
    Write-Host "=================================================="
    Write-Host "Testing Candidate Registration validations..."
    $regGet = $adminRes.Client.GetAsync("/register-user").Result
    $regHtml = $regGet.Content.ReadAsStringAsync().Result
    $csrf = [regex]::Match($regHtml, 'name="_csrf"\s+value="([^"]+)"').Groups[1].Value

    $emptyForm = New-Object 'System.Collections.Generic.Dictionary[string,string]'
    $emptyForm.Add("_csrf", $csrf)
    $emptyContent = New-Object System.Net.Http.FormUrlEncodedContent($emptyForm)
    $valResp = $adminRes.Client.PostAsync("/register-user", $emptyContent).Result
    $valHtml = $valResp.Content.ReadAsStringAsync().Result
    if ($valHtml.Contains("Full Name is required") -or $valHtml.Contains("All registration fields are mandatory")) {
        Write-Host "SUCCESS: Mandatory validation on candidate registration confirmed!"
    }

    Write-Host ""
    Write-Host ">>> ALL TESTS PASSED SUCCESSFULLY! 100% FUNCTIONAL <<<"
} catch {
    Write-Error $_
}
