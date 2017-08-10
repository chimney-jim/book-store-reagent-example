(ns react-example-cljs.prod
  (:require [react-example-cljs.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
