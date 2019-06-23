# Passwords and keys from SSM
export API_KEY=$(aws ssm get-parameters --name /${ENV}/policy/api-key --with-decryption --region eu-west-1 --query Parameters[0].Value --output text)

# Run app
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar