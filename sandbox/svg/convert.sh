#!/bin/sh

rm -rf drawable-mdpi drawable-hdpi drawable-xhdpi drawable-xxhdpi
mkdir drawable-mdpi drawable-hdpi drawable-xhdpi drawable-xxhdpi

for i in *.svg
do
    k=${i%%.*}
    inkscape -z -e drawable-xxhdpi/$k.png -d 90 $k.svg
    inkscape -z -e drawable-xhdpi/$k.png -d 60 $k.svg
    inkscape -z -e drawable-hdpi/$k.png -d 45 $k.svg
    inkscape -z -e drawable-mdpi/$k.png -d 30 $k.svg
done
