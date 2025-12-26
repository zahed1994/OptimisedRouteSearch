# Git commit and push script for Optimal Route Finder project

Write-Host "Optimal Route Finder - Git Commit and Push Script" -ForegroundColor Green
Write-Host "=" * 60

# Navigate to project directory
cd "C:\Users\asus\IdeaProjects\optimalroutefinder"

# Add all files to staging
Write-Host "Adding files to git staging area..." -ForegroundColor Yellow
git add .

# Commit with descriptive message
Write-Host "Committing files..." -ForegroundColor Yellow
git commit -m "Initial commit: Optimal Route Finder Scala project with Dijkstra, BFS, and A* algorithms"

# Check git status
Write-Host "Current git status:" -ForegroundColor Yellow
git status

# Push to remote repository
Write-Host "Pushing to GitHub repository..." -ForegroundColor Yellow
git push -u origin master

Write-Host "Successfully pushed to GitHub!" -ForegroundColor Green
Write-Host "Repository URL: https://github.com/zahed1994/OptimisedRouteSearch" -ForegroundColor Cyan

