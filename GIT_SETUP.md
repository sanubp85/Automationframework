# Git Setup Guide for Azure DevOps

## Step 1: Initialize Git Repository

Open a terminal in your project root directory and run:

```bash
git init
```

This creates a hidden `.git` folder that tracks all version history for your project.

---

## Step 2: Add All Files

Stage all project files for your first commit:

```bash
git add .
```

> **Tip:** Run `git status` first to review which files will be tracked.

---

## Step 3: Create Initial Commit

Commit the staged files with a descriptive message:

```bash
git commit -m "Initial commit"
```

---

## Step 4: Create Repository on Azure DevOps

1. Sign in to [Azure DevOps](https://dev.azure.com)
2. Navigate to your **Organization** → **Project**
3. In the left sidebar, click **Repos**
4. Click **+ New repository** (or use the repository dropdown at the top)
5. Fill in the repository details:
   - **Repository type:** Git
   - **Repository name:** `<your-repo-name>`
   - Leave **"Add a README"** unchecked (since you already have local files)
6. Click **Create**
7. Copy the **HTTPS** clone URL shown on the next screen

---

## Step 5: Add Remote Repository

Link your local repository to Azure DevOps:

```bash
git remote add origin https://dev.azure.com/<organization>/<project>/_git/<repository>
```

Replace `<organization>`, `<project>`, and `<repository>` with your actual Azure DevOps values.

**Verify the remote was added:**

```bash
git remote -v
```

---

## Step 6: Push to Remote Repository

Push your local commits to Azure DevOps:

```bash
git push -u origin main
```

> **Note:** If your default branch is `master` instead of `main`, use `git push -u origin master`.  
> Azure DevOps may prompt for credentials — sign in with your Microsoft account or a **Personal Access Token (PAT)**.

---

## Common Git Commands

### Check Status
```bash
git status
```

### Add Changes
```bash
# Add a specific file
git add <filename>

# Add all changes
git add .
```

### Commit Changes
```bash
git commit -m "Your commit message"
```

### Push Changes
```bash
git push origin main
```

### Pull Latest Changes
```bash
git pull origin main
```

### Create New Branch
```bash
git checkout -b <branch-name>
```

### Switch Branch
```bash
git checkout <branch-name>
```

### View Branches
```bash
# Local branches
git branch

# All branches (local + remote)
git branch -a
```

---

## Files Excluded from Git (.gitignore)

Create a `.gitignore` file in your project root with the following entries:

```
# Maven build files
target/

# Allure test reports
allure-results/
allure-report/

# IDE settings
.idea/

# IntelliJ project files
*.iml

# TestNG reports
test-output/

# Log files
*.log
```

> **Tip:** Add and commit your `.gitignore` file before adding other project files to ensure excluded items are never accidentally tracked.

---

## Azure DevOps Authentication

### Option 1: Personal Access Token (PAT) — Recommended
1. In Azure DevOps, click your profile icon → **Personal access tokens**
2. Click **+ New Token**
3. Set **Scopes** → **Code** → **Read & write**
4. Copy the token and use it as your password when Git prompts for credentials

### Option 2: Git Credential Manager
Install [Git Credential Manager](https://github.com/GitCredentialManager/git-credential-manager) to handle Azure DevOps authentication automatically via browser sign-in.
