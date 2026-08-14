#!/usr/bin/env bash
set -e

MAPS_DIR=$(dirname "$0")/..

if [[ "$GITHUB_REF_TYPE" != 'branch' ]]; then
	echo "Skipping pushing of changes since we are not on a branch"
	exit 0
fi

if [[ `git status --porcelain "${MAPS_DIR}"` ]]; then

	if [[ "$GITHUB_REF_NAME" ]]; then
		if [ "$GITHUB_REF_NAME" = "development" ] \
			|| [ "$GITHUB_REF_NAME" = "beta" ] \
			|| [ "$GITHUB_REF_NAME" = "master" ] \
			|| [ "$GITHUB_REF_NAME" = "gh-actions-push-test" ]
		then
			echo "Will try to commit and push changes on '${GITHUB_REF_NAME}' branch"
			
		else
			echo "Branch '${GITHUB_REF_NAME}' is not configured for auto commit and push"
			exit 0
		fi
	else
		echo "GITHUB_REF_NAME not set; will not commit and push changes"
		exit 0
	fi

	if ! [[ "$GH_TOKEN" ]] || ! [[ "$GH_USER" ]]; then
		echo "Error: GH_TOKEN and/or GH_USER not set"
		exit 1
	fi
		
	git config --global user.name "clarin-deploy"

	echo "Committing changes"
	
	if [ "$GIT_COMMIT_USER_EMAIL" ]; then
		git config user.email "$GIT_COMMIT_USER_EMAIL"
	fi
	
	if [ "$GIT_COMMIT_USER_NAME" ]; then
		git config user.name "$GIT_COMMIT_USER_NAME"
	fi
	
	git checkout "${GITHUB_REF_NAME}"
	
	#commit
	git commit "${MAPS_DIR}" -m "Automatic commit of built changes in value maps (GitHub job ${GITHUB_JOB})"
	
	#push
	URL=`git remote get-url origin`
	echo "Remote: ${URL}"
	git remote add target "https://${GH_USER}:${GH_TOKEN}@${URL#https://}"
	git push -q target 
else
	echo "Nothing to commit"
fi
