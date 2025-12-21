#!/bin/bash

# Exit on error
set -e

# Configuration - UPDATE THESE
PROJECT_ID="joi-jinko"
REGION="us-central1"
BACKEND_SERVICE_NAME="simulator-backend"
FRONTEND_SITE_ID="joi-jinko"

echo "Using Project ID: $PROJECT_ID"

# 1. Build and Push Backend to Artifact Registry
echo "Building backend Docker image..."
cd simulator/backend
gcloud auth configure-docker us-central1-docker.pkg.dev --quiet
docker build -t us-central1-docker.pkg.dev/$PROJECT_ID/ocpj21-simulator/$BACKEND_SERVICE_NAME:latest .
docker push us-central1-docker.pkg.dev/$PROJECT_ID/ocpj21-simulator/$BACKEND_SERVICE_NAME:latest

# 2. Deploy Backend to Cloud Run
echo "Deploying backend to Cloud Run..."
gcloud run deploy $BACKEND_SERVICE_NAME \
  --image us-central1-docker.pkg.dev/$PROJECT_ID/ocpj21-simulator/$BACKEND_SERVICE_NAME:latest \
  --platform managed \
  --region $REGION \
  --allow-unauthenticated \
  --set-env-vars="SPRING_PROFILES_ACTIVE=prod"

# Get the Cloud Run URL
BACKEND_URL=$(gcloud run services describe $BACKEND_SERVICE_NAME --platform managed --region $REGION --format 'value(status.url)')
echo "Backend deployed at: $BACKEND_URL"

# 3. Update Frontend with Backend URL
echo "Updating frontend environment..."
cd ../frontend
sed -i '' "s|YOUR_CLOUD_RUN_BACKEND_URL|$BACKEND_URL|g" src/environments/environment.prod.ts

# 4. Build Frontend
echo "Building frontend..."
npm install
npm run build -- --configuration production

# 5. Deploy Frontend to Firebase Hosting
echo "Deploying frontend to Firebase Hosting..."
# Ensure you have firebase-tools installed: npm install -g firebase-tools
firebase deploy --only hosting --project $PROJECT_ID

echo "Deployment complete!"
