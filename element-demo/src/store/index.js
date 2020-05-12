import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        routes: [],
        currentUser: JSON.parse(window.sessionStorage.getItem("user")),
    },
    mutations: {
        initCurrentUser(state, res) {
            state.currentUser = res;
        },
        initRoutes(state, data) {
            state.routes = data;
        },
    }
})

export default store;