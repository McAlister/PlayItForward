//= wrapped

angular // jshint ignore:line
    .module("playitforward.index")
    .directive("onErrorSrc",function(){

        return {
            link: function(scope, element, attrs) {
                element.bind('error', function() {
                    if (attrs.src != attrs.onErrorSrc) {
                        attrs.$set('src', attrs.onErrorSrc);
                    }
                });
            }
        }

});