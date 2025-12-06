#!/usr/bin/env fish

read -P "day> " day
read -P "part> " part
mkdir src/main/resources/2025/day$day/
if test "$part" = "1"
    read -P "Press enter once the correct sample INPUT is in your clipboard"
    pbpaste | tee src/main/resources/2025/day$day/sample.input
end

read -P "Press enter once the correct sample ANSWER is in your clipboard"
pbpaste | tee src/main/resources/2025/day$day/part{$part}_sample.answer
