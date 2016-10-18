# om-html

[hiccup](https://github.com/weavejester/hiccup)/[sablono](https://github.com/r0man/sablono) for Om Next.

Now that Om Next supports server-side rendering, this gives client+server rendering for free.

# Usage

```clojure
(:require [om-html.html :as html])

(defui Page
  Object
  (render [this]
    (html/html
      [:h1 "Hello World"])))
```

- `(html/html)` is a function that returns calls to `om.dom/<element>`. It works in both `.clj` and `.cljs`.
- All the standard hiccup/sablono syntax is supported: `[:div#foo.bar]`, implicit divs: `[:.foo]`.


# Server-Side Rendering

- Use an Om Next release with SSR support (`1.0.0-alpha45` or higher).
- Follow https://anmonteiro.com/2016/09/om-next-server-side-rendering/