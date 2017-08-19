(ns react-example-cljs.book-page
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-uuid-utils.core :as uuid]
            [ajax.core :as ajax]))

(defn make-book [title]
  {:id (uuid/make-random-uuid)
   :title title})

(def json "[{\"id\":1,\"title\":\"Dune\"},{\"id\":2,\"title\":\"The Jazz of Physics\"},{\"id\":3,\"title\":\"Peopleware: Productive Projects and Teams\"},{\"id\":4,\"title\":\"Rework\"}]")
(def books
  (as-> json $
    (.parse js/JSON $)
    (js->clj $ :keywordize-keys true)))
(def books-clj
  [ {:id 1 :title "Dune"}
    {:id 2,:title "The Jazz of Physics"}
    {:id 3,:title "Peopleware: Productive Projects and Teams"}
    {:id 4,:title "Rework"}])

(rf/reg-event-db
  ::get-books
  (fn [_ _]
    {:http-xhrio {:method           :get
                  :uri              "http://5983c991409c6600117a6c06.mockapi.io/api/books/books"
                  :with-credentials false
                  :response-format  (ajax/json-response-format {:keyword? true})
                  :on-success       [::get-books-success]
                  :on-failure       [::get-books-failure]}}))

(rf/reg-event-db
  ::get-books-success
  (fn [db [_ results]]
    (println "Found some books!")
    (update db :books assoc (map #(make-book (:title %)) results))))

(rf/reg-event-fx
  ::get-books-failure
  (fn [_ _]
    (println "Failed to get some books!")))

(rf/reg-event-db
  ::add-book
  (fn [db [_ title]]
    (update db :books conj (make-book title))))

(rf/reg-sub
  ::books
  (fn [db _]
    (println (:books db))
    (:books db)))


(defn book-page []
  ;;TODO: this thing failes when executing.  with some data it puts a weird map.  with the http call it just kills the page
  (rf/dispatch [::get-books-success books-clj])
  (let [books (rf/subscribe [::books])
        book-input (r/atom "hello")]
    (fn []
      [:div
        (str @books)
        [:div
          [:h3 "Book Form"]
          [:div
            [:input {:type "text"
                     :name "title"
                     :value @book-input
                     :on-change (fn [el]
                                  (.preventDefualt el)
                                  (reset! book-input (-> el .-target .-value)))}]
            [:button
              {:type "submit"
               :on-click (fn [el]
                            (.preventDefault el)
                            (rf/dispatch [:add-book @book-input]))}
              "Save"]]
          [:div
            [:h3 "Books"]
            [:ul
              (for [book @books]
                ^{:key book} [:li (:title book)])]]]])))
