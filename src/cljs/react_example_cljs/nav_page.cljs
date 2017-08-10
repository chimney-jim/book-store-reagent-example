(ns react-example-cljs.nav-page)

(defn nav-page []
  [:nav {:className "navbar navbar-toggleable-md navbar-light bg-faded"}
    [:div {:className "navbar-header"}
      [:a {:className "navbar-brand" :href "#"} "Scotch Books"]]
    [:div {:className "collapse navbar-collapse" :id "example-navbar"}
      [:ul {:className "navbar-nav"}
        [:li {:className "nav-item"}
          [:a {:className "nav-link" :href "/"} "Home"]]
        [:li {:className "nav-item"}
          [:a {:className "nav-link" :href "/about"} "About"]]
        [:li {:className "nav-item"}
          [:a {:className "nav-link" :href "/books"} "Books"]]]]])
