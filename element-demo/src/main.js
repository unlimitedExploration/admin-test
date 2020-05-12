import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import {getRequest} from './utils/api'
import {postRequest} from './utils/api'
import {postKeyValueRequest} from './utils/api'
import {deleteRequest} from './utils/api'
import {putRequest} from './utils/api'
import {initMenu} from "./utils/menus";

Vue.config.productionTip = false
Vue.use(ElementUI)

Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.postKeyValueRequest = postKeyValueRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.putRequest = putRequest;

router.beforeEach((to, from, next)=> {
  if (to.path == '/') {
    next();
  }else {
    if (window.sessionStorage.getItem("user")) {
        initMenu(router, store);
        next();
    }else{
        next('/?redirect='+to.path);
    }
  }
})

// new Vue({
//   el: '#app',
//   router,
//   store,
//   template: '<App/>',
//   components: {App}
// })

window.bus = new Vue();
new Vue({
  router,
  store,
  render:h => h(App)
}).$mount("#app")
