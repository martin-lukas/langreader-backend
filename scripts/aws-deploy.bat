scp -i %USERPROFILE%\.ssh\langreader-ec2.pem ^
        backend\target\langreader.jar ^
        ubuntu@ec2-35-159-48-166.eu-central-1.compute.amazonaws.com:~

ssh -i %USERPROFILE%\.ssh\langreader-ec2.pem ^
        ubuntu@ec2-35-159-48-166.eu-central-1.compute.amazonaws.com ^
        "bash -s" < scripts\aws-remote-run.sh
