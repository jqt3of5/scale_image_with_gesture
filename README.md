# Pinch to Zoom ImageView Using Matrix
I spent so long trying to figure out how to implement pinch to zoom on an image view. Android already has libraries for responding to gestures, but it wasn't at all clear how to take the scalefactor and focusx/y properties and use them to zoom an image.

Basically what I wanted was the middle point between two your two fingers to be the pivot point that the image scaled around. I tried a number of different ways to calculate the pivot point based on the matrix/scale factor etc. But they all were just slightly wrong. The actual pivot point would end up moving around and not stay between the two fingers at every zoom level. Eventually I decided to try a vauge notion that I had. *Matrix.postScale()* did the trick. The exact details of the math I'm a little fuzzy on, but my best understanding is that this takes as parameters transformations to the matrix and adds them to the matrix. Totally nagating the need to do the calculations yourself. 
