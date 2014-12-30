#!/bin/bash

rm -rf /tmp/anyfetch-assets;
git clone git@github.com:AnyFetch/anyfetch-assets.git /tmp/anyfetch-assets;

for id in `ls /tmp/anyfetch-assets/images/providers | grep -v grayscale | sed "s/\.svg//g"`;
do
    convert -resize 48x48 /tmp/anyfetch-assets/images/providers/${id}.svg ./commons/src/main/res/drawable-mdpi/ic_provider_${id}.png;
    convert -resize 72x72 /tmp/anyfetch-assets/images/providers/${id}.svg ./commons/src/main/res/drawable-hdpi/ic_provider_${id}.png;
    convert -resize 96x96 /tmp/anyfetch-assets/images/providers/${id}.svg ./commons/src/main/res/drawable-xhdpi/ic_provider_${id}.png;
    convert -resize 144x144 /tmp/anyfetch-assets/images/providers/${id}.svg ./commons/src/main/res/drawable-xxhdpi/ic_provider_${id}.png;
done;
