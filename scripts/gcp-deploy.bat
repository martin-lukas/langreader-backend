gcloud compute scp ..\target\langreader-backend.jar ^
        langreader-main:langreader-backend.jar ^
        --zone "europe-west1-b" --project "enhanced-optics-350517"

:: gcloud compute ssh --zone "europe-west1-b" "langreader-main" --project "enhanced-optics-350517"