# om-html

[hiccup](https://github.com/weavejester/hiccup)/[sablono] for Om Next.

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

# Server-Side Rendering

- Use an Om Next release with SSR support (`1.0.0-alpha45` or higher).
- Follow https://anmonteiro.com/2016/09/om-next-server-side-rendering/
