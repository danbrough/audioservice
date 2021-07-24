

//var links = document.getElementsByClassName('u-blocklink__overlay-link');
//var links = document.querySelectorAll('a.u-blocklink__overlay-link[data-player]');
var links = document.querySelectorAll('a[data-player]');

for(i = 0; i < links.length; i++){
  audienz.debug('found link: ' + links[i].getAttribute('href') + ' data-player: ' + links[i].getAttribute('data-player') );
  /*var linkRef = links[i].getAttribute('href');
  var dataPlayer = links[1].getAttribute('data-player');
  */

  links[i].removeAttribute('href');

  links[i].onclick=function(e){
    var dataPlayer = this.getAttribute('data-player');
    var linkRef = this.getAttribute('href');
    audienz.warn('clicked:' + dataPlayer+ ' href:' + linkRef);
    e.stopPropagation();
    audienz.mediaLink(dataPlayer);
  }
}


function removeContent(selector){
    var items = document.querySelectorAll(selector);
    for(var i = 0; i < items.length; i++){
        items[i].parentElement.removeChild(items[i]);
    }
}



//removeContent('.site-header,.site-footer,.c-social__menu,.article__footer');

function removeClass(clz){
   var items = document.querySelectorAll('.' + clz);
   for(var i = 0; i < items.length; i++){
       items[i].classList.remove(clz);
   }
}

removeClass('overflow-container');
removeClass('c-quick-six__list');
removeClass('c-related__list');


audienz.debug('finished js');