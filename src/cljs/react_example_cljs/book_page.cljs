(ns react-example-cljs.book-page
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-uuid-utils.core :as uuid]
            [ajax.core :as ajax]))

(defn make-book [title]
  {:id (uuid/make-random-uuid)
   :title title})

(rf/reg-event-fx
  ::get-books
  (fn [_ _]
    {:http-xhrio {:method           :get
                  :uri              "http://5983c991409c6600117a6c06.mockapi.io/api/books/books"
                  :response-format  (ajax/json-response-format {:keywords? true})
                  :on-success       [::get-books-success]
                  :on-failure       [::get-books-failure]}}))

(rf/reg-event-db
  ::get-books-success
  (fn [db [_ results]]
    (assoc db :books (map #(make-book (:title %)) results))))

(rf/reg-event-fx
  ::get-books-failure
  (fn [_ [_ method url results]]
    (println "Failed to get some books!")))

(rf/reg-event-db
  ::add-book
  (fn [db [_ title]]
    (update db :books conj (make-book title))))

(rf/reg-sub
  ::books
  (fn [db _]
    (:books db)))

(defn book-page []
  (let [books (rf/subscribe [::books])
        book-input (r/atom "hello")]
    (r/create-class
     {:component-will-mount
      #(rf/dispatch [::get-books])

      :render
      (fn []
        [:div
         [:div
          [:h3 "Book Form"]
          [:div
           [:input {:type "text"
                    :name "title"
                    :value @book-input
                    :on-change #(reset! book-input (-> % .-target .-value))}]
           [:button
            {:type "submit"
             :on-click #(rf/dispatch [::add-book @book-input])}
            "Save"]]
          [:div
           [:h3 "Books"]
           [:ul
            (for [book @books]
              ^{:key book} [:li (:title book)])]]]])})))
