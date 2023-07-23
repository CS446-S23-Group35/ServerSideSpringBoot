SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
TAG=$(python3 $SCRIPT_DIR/getNextTag.py $( docker images --format "{{json . }}" 068734148537.dkr.ecr.us-east-2.amazonaws.com/wastenot-springboot))
$SCRIPT_DIR/build-docker.sh $TAG
$SCRIPT_DIR/push-docker.sh $TAG
