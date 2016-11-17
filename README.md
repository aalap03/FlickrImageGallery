# FlickrImageGallery
Search images in using flickr api and demonstrated swipe gallery feature using pager adapter.

This simple photo searching app has a search bar in first screen. I display pictures from flickr using tag text entered by user here.
Second screen displays list of pictures with their information like thumbnail, caption and authorname in a recycler view. Back button 
on the head returns to photo search screen. Clicking any picture takes us fullscreen picture with largetst possible size received from 
flickr api response. At the top of the screen you can see the photo counter. You can swipe left and right to see other pictures in the list. 

Updated code with extra functionality to this with deleting feature. Delete button will delete the picture from current screen as well as 
from the list of second screen. Pressing back button on screen 3 means you have an updated list in screen 2 with removed deleted item(s).
Going back and forth between second and third screen with deleting pictures preserves their status. When we make search again ofcourse the 
deleted pictures' info will be lost and with whole new set of data recycler view will be loaded.

Applied animation using animation library on the recycler view of image lists. Explored flickr apis.
