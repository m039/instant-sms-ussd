#!/bin/sh


FILE=./data/export.png

convert -resize 36x36 ${FILE} ./res/drawable-ldpi/icon.png
convert -resize 48x48 ${FILE} ./res/drawable-mdpi/icon.png
convert -resize 72x72 ${FILE} ./res/drawable-hdpi/icon.png
https://market.android.com/publish/Home#AppEditorPlace:p=com.m039.mqst