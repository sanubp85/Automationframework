# Git Repository Setup Guide

## Step 1: Initialize Git Repository

```bash
cd "c:\Users\Prasanna Kumar\OneDrive - vRize India Private Ltd\Documents\Tonic\Tonic BOH\BOHTONICDec2025\Template"
git init
```

## Step 2: Add All Files

```bash
git add .
```

## Step 3: Create Initial Commit

```bash
git commit -m "Initial commit: Playwright Cucumber TestNG Framework"
```

## Step 4: Create Repository on GitHub/GitLab/Bitbucket

- Go to GitHub.com (or your Git provider)
- Click "New Repository"
- Name it (e.g., "playwright-automation-framework")
- Don't initialize with README (we already have one)
- Copy the repository URL

## Step 5: Add Remote Repository

```bash
git remote add origin <YOUR_REPOSITORY_URL>
```

Example:
```bash
git remote add origin https://github.com/username/playwright-automation-framework.git
```

## Step 6: Push to Remote Repository

```bash
git branch -M main
git push -u origin main
```

## Common Git Commands

### Check Status
```bash
git status
```

### Add Changes
```bash
git add .
```

### Commit Changes
```bash
git commit -m "Your commit message"
```

### Push Changes
```bash
git push
```

### Pull Latest Changes
```bash
git pull
```

### Create New Branch
```bash
git checkout -b feature/branch-name
```

### Switch Branch
```bash
git checkout branch-name
```

### View Branches
```bash
git branch
```

## Files Excluded from Git (.gitignore)

- target/ (Maven build files)
- allure-results/ (Test reports)
- allure-report/ (Generated reports)
- .idea/ (IDE settings)
- *.iml (IntelliJ files)
- test-output/ (TestNG reports)
- *.log (Log files)
