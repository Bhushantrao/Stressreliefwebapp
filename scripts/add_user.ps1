# ===================================================================
# StressLess - Interactive User Registration Script
# Adds a new user from terminal input or parameters, registers via API,
# and verifies login and JWT generation.
# ===================================================================

param(
    [string]$Name,
    [string]$Email,
    [string]$Password,
    [string]$Role = "ROLE_USER"
)

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   StressLess Platform - New User Onboarding CLI" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

# Prompt for Name if not provided
if (-not $Name) {
    do {
        $Name = Read-Host "Enter Full Name"
        if (-not $Name.Trim()) {
            Write-Host "Name cannot be empty." -ForegroundColor Yellow
        }
    } while (-not $Name.Trim())
}

# Prompt for Email if not provided
if (-not $Email) {
    do {
        $Email = Read-Host "Enter Email Address"
        if (-not $Email.Trim() -or -not ($Email -match "^[^@]+@[^@]+\.[^@]+$")) {
            Write-Host "Please enter a valid email address." -ForegroundColor Yellow
        }
    } while (-not $Email.Trim() -or -not ($Email -match "^[^@]+@[^@]+\.[^@]+$"))
}

# Prompt for Password if not provided
if (-not $Password) {
    do {
        $Password = Read-Host "Enter Password (min 6 characters)"
        if ($Password.Length -lt 6) {
            Write-Host "Password must be at least 6 characters long." -ForegroundColor Yellow
        }
    } while ($Password.Length -lt 6)
}

# Prompt for Role if not specified or default
if (-not $PSBoundParameters.ContainsKey('Role')) {
    $roleInput = Read-Host "Enter Role [1: ROLE_USER (Default), 2: ROLE_ADMIN]"
    if ($roleInput -eq "2" -or $roleInput -eq "ROLE_ADMIN") {
        $Role = "ROLE_ADMIN"
    } else {
        $Role = "ROLE_USER"
    }
}

Write-Host ""
Write-Host "Registering user: " -NoNewline
Write-Host "$Name ($Email)" -ForegroundColor Yellow
Write-Host "Assigned Role: " -NoNewline
Write-Host "$Role" -ForegroundColor Magenta
Write-Host ""

$registerPayload = @{
    name = $Name.Trim()
    email = $Email.Trim().ToLower()
    password = $Password
    role = $Role
} | ConvertTo-Json

$backendUrl = "http://localhost:8081/api"

try {
    # 1. Register User
    $regResponse = Invoke-RestMethod -Uri "$backendUrl/auth/register" `
                                     -Method Post `
                                     -Body $registerPayload `
                                     -ContentType "application/json" `
                                     -ErrorAction Stop

    Write-Host "[SUCCESS] $($regResponse.message)" -ForegroundColor Green
    Write-Host "User ID: $($regResponse.data)" -ForegroundColor Gray

    # 2. Verify Login
    Write-Host ""
    Write-Host "Verifying login with newly created credentials..." -ForegroundColor Cyan

    $loginPayload = @{
        email = $Email.Trim().ToLower()
        password = $Password
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$backendUrl/auth/login" `
                                       -Method Post `
                                       -Body $loginPayload `
                                       -ContentType "application/json" `
                                       -ErrorAction Stop

    if ($loginResponse.success) {
        $token = $loginResponse.data.token
        $userId = $loginResponse.data.id
        $userRole = $loginResponse.data.role
        
        Write-Host "[SUCCESS] Authentication successful!" -ForegroundColor Green
        Write-Host "JWT Token issued: " -NoNewline
        Write-Host "$($token.Substring(0, [Math]::Min(30, $token.Length)))..." -ForegroundColor Yellow
        Write-Host "Session ready for User ID: $userId ($userRole)" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "You can now log in at: http://localhost:4200/login" -ForegroundColor Green
        Write-Host "Credentials:" -ForegroundColor White
        Write-Host "  Email:    $($Email.Trim().ToLower())" -ForegroundColor White
        Write-Host "  Password: $Password" -ForegroundColor White
    }
} catch {
    Write-Host "[ERROR] Failed to register user:" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errBody = $reader.ReadToEnd()
        Write-Host $errBody -ForegroundColor Red
    } else {
        Write-Host $_.Exception.Message -ForegroundColor Red
    }
}
Write-Host "==========================================================" -ForegroundColor Cyan
