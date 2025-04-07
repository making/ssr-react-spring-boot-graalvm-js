import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import './index.css'
import routes from "./routes.tsx";

// Get initial data from server
const initDataText = document.getElementById('__INIT_DATA__');
const initData = initDataText && initDataText.textContent ? JSON.parse(initDataText.textContent) : {};
const router = createBrowserRouter(routes(initData));

// Hydrate the application
ReactDOM.hydrateRoot(
  document.getElementById('root')!,
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
)
