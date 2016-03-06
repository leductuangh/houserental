#!/bin/bash
echo "Updating ActiveAndroid network library..."
if [ -d "ActiveAndroid" ]; then
	echo "ActiveAndroid directory already exists, updating..."
	cd ActiveAndroid
	git pull
	if ! [ $? -eq 0 ]; then
		echo "Failed to pull ActiveAndroid library from master, please try again!"
		exit;
	fi
	cd ..
	cp -r ActiveAndroid/src/* src/main/java/
	wait
	echo "Update ActiveAndroid library successfully!"
	exit;
fi

echo "Cloning master ActiveAndroid..."
git clone https://github.com/pardom/ActiveAndroid.git
if ! [ $? -eq 0 ]; then
	echo "Failed to clone ActiveAndroid library!"
	exit;
else
	echo "Clone ActiveAndroid library successfully! Updating..."
	cd ActiveAndroid
	git pull
	cd ..
	cp -r ActiveAndroid/src/* src/main/java/
	echo "Update ActiveAndroid library successfully!"
fi
exit;
