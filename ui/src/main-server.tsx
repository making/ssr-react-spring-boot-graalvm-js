import React from 'react'
import ReactDOMServer from 'react-dom/server'
import './index.css'
import routes from "./routes.tsx";
import {StaticRouter} from "react-router-dom/server";
import {Route, Routes} from "react-router-dom";
import {RouteProps} from "react-router/dist/lib/components";

export function render(url: string, input: string) {
    const initData = input ? JSON.parse(input) : {};
    const router = routes(initData);
    return {
        html: ReactDOMServer.renderToString(
            <React.StrictMode>
                <StaticRouter location={url}>
                    <Routes>
                        {router.map(route => <Route {...route as RouteProps} />)}
                    </Routes>
                </StaticRouter>
            </React.StrictMode>,
        )
    };
}
