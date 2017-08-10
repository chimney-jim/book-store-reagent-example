(ns ^:figwheel-no-load react-example-cljs.dev
  (:require
    [react-example-cljs.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
