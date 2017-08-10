(ns react-example-cljs.book-page
  (:require [reagent.core :refer [atom]]))

(defn book-page [app-state]
  (let [books (:books @app-state)
        book-input (atom "hello")]
    (fn []
      [:div
        (str books)
        [:div
          [:h3 "Books"]
          [:ul
            (for [book books]
              ^{:key book} [:li (:title book)])]]
        [:div
          [:h3 "Book Form"]
          [:div
            [:input {:type "text"
                     :name "title"
                     :value @book-input
                     :on-change #(reset! book-input (-> % .-target .-value))}]
            [:button
              {:type "submit"
               :on-click #(swap! app-state assoc-in [:books] (conj books {:title @book-input}))}
              "Save"]]]])))
