(ns om-html.html
  (:require [clojure.string :as str]
            [clojure.set :refer [rename-keys]]
            [clojure.spec :as s]
            [om.dom :as dom])
  #?(:cljs (:require-macros [om-html.html :refer (html)])))

;; hiccup-alike DSL, for Om.next

(declare html*)

(defn strip-css
  "Strip the # and . characters from the beginning of `s`."
  [s] (if s (str/replace s #"^[.#]" "")))

(defn compact-map
  "Removes all map entries where the value of the entry is empty."
  [m]
  (reduce
   (fn [m k]
     (let [v (get m k)]
       (if (empty? v)
         (dissoc m k) m)))
   m (keys m)))

(defn merge-with-class
  "Like clojure.core/merge but concatenate :class entries."
  [& maps]
  (let [classes (->> (mapcat #(cond
                               (list? %1) [%1]
                               (sequential? %1) %1
                               :else [%1])
                             (map :class maps))
                     (remove nil?) vec)
        maps (apply merge maps)]
    (if (empty? classes)
      maps
      (assoc maps :class classes))))

(defn match-tag
  "Match `s` as a CSS tag and return a vector of tag name, CSS id and
  CSS classes."
  [s]
  (let [matches (re-seq #"[#.]?[^#.]+" (name s))
        [tag-name names] (cond (empty? matches)
                               (throw (ex-info (str "Can't match CSS tag: " s) {:tag s}))
                               (#{\# \.} (ffirst matches)) ;; shorthand for div
                               ["div" matches]
                               :default
                               [(first matches) (rest matches)])]
    [tag-name
     (first (map strip-css (filter #(= \# (first %1)) names)))
     (vec (map strip-css (filter #(= \. (first %1)) names)))]))


(defn normalize-element
  "Ensure an element vector is of the form [tag-name attrs content]."
  [[tag & content]]
  (when (not (or (keyword? tag) (symbol? tag) (string? tag)))
    (throw (ex-info (str tag " is not a valid element name.") {:tag tag :content content})))
  (let [[tag id class] (match-tag tag)
        tag-attrs (compact-map {:id id :class class})
        map-attrs (first content)
        [tag attrs content] (if (and (map? map-attrs)
                                     (not (record? map-attrs)))
                              [tag (merge-with-class tag-attrs map-attrs) (next content)]
                              [tag tag-attrs content])
        attrs (if (:class attrs)
                (do
                  (assert (coll? (:class attrs)))
                  (update attrs :class #(str/join " " %)))
                attrs)]
    [tag attrs content]))

(defn update-attrs [attrs]
  (rename-keys attrs {:class :className
                      :for :htmlFor}))

(s/def ::tag keyword?)
(s/def ::attrs map?)
(s/def ::content (s/* any?))
(s/def ::html-vec (s/cat :tag ::tag :attrs (s/? ::attrs) :content ::content))

(s/fdef eval-vector :args (s/cat :expr ::html-vec))
(defn eval-vector [expr]
  (let [[tag attrs content] (normalize-element expr)
        f (symbol "om.dom" tag)]
    #?(:clj `(apply ~f ~(update-attrs attrs) ~(apply html* content))
       :cljs `(apply ~f (clj->js ~(update-attrs attrs)) ~(apply html* content)))))

(defn html* [& content]
  (->>
   content
   (mapv (fn [expr]
           (cond
             (vector? expr) (eval-vector expr)
             :else expr)))))

#?(:clj
   (do
     ;;(s/fdef html :args (s/cat :v ::html-vec))
     (defmacro html
       "Takes a hiccup-style vector of HTML, returns om.dom elements"
       [v]
       ;; macro so we return calls to e.g. om.dom/div, for CLJS advanced compilation
       (first (apply html* [v])))))
