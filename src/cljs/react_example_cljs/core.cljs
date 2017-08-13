(ns react-example-cljs.core
    (:require [reagent.core :as reagent :refer [atom cursor]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [react-example-cljs.home-page :refer [home-page]]
              [react-example-cljs.book-page :refer [book-page]]
              [react-example-cljs.about-page :refer [about-page]]
              [react-example-cljs.nav-page :refer [nav-page]]
              [re-frame.core :as rf]))

(rf/reg-event-db
  :init-db
  (fn [_ _]
    {:current-page [home-page]
     :books [{:id (gensym "book_")
              :title "Dune"}
             {:id (gensym "book_")
              :title "Something"}
             {:id (gensym "book_")
              :title "something else"}]}))

(rf/reg-event-db
  :set-current-page
  (fn [db [_ page]]
    (assoc db :current-page page)))

(rf/reg-sub
  :current-page
  (fn [db _]
    (:current-page db)))

;; -------------------------
;; Routes

(defn current-page []
  (let [cur-page (rf/subscribe [:current-page])]
    (fn []
      [:div
        [nav-page]
        [:main.container @cur-page]])))

(secretary/defroute "/" []
  (rf/dispatch [:set-current-page [home-page]]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-current-page [about-page]]))

(secretary/defroute "/books" []
  (rf/dispatch [:set-current-page [book-page]]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (do
    (rf/dispatch [:init-db])
    (reagent/render [current-page] (.getElementById js/document "app"))))

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
