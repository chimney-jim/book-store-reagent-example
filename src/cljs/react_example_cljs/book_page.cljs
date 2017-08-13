(ns react-example-cljs.book-page
  (:require [reagent.core :refer [atom]]
            [re-frame.core :as rf]))

(rf/reg-event-db
  :add-book
  (fn [db [_ title]]
    (update db :books conj {:id (gensym "book_")
                            :title title})))

(rf/reg-sub
  :books
  (fn [db _]
    (:books db)))

(defn book-page []
  (let [books (rf/subscribe [:books])
        book-input (atom "hello")]
    (fn []
      [:div
        (str @books)
        [:div
          [:h3 "Books"]
          [:ul
            (for [book @books]
              ^{:key book} [:li (:title book)])]]
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
              "Save"]]]])))
