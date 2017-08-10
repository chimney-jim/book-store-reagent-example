(ns react-example-cljs.core
    (:require [reagent.core :as reagent :refer [atom cursor]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [react-example-cljs.home-page :refer [home-page]]
              [react-example-cljs.book-page :refer [book-page]]
              [react-example-cljs.about-page :refer [about-page]]
              [react-example-cljs.nav-page :refer [nav-page]]))

(def app-state (atom {:page [home-page]
                      :books [{:title "Dune"}
                              {:title "Something"}
                              {:title "something else"}]}))

;; -------------------------
;; Routes

(defn current-page []
  [:div [nav-page]
    [:main.container (:page @app-state)]])

(secretary/defroute "/" []
  (swap! app-state assoc-in [:page] [home-page]))

(secretary/defroute "/about" []
  (swap! app-state assoc-in [:page] [about-page]))

(secretary/defroute "/books" []
  (swap! app-state assoc-in [:page] [book-page app-state]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
