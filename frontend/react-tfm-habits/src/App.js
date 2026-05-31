// src/App.js
import './assets/styles/reset.css';
import './assets/styles/global.css';
import './assets/styles/tokens.css';
import './App.css';

import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from './context/AuthContext';
import AppRouter from './routes/AppRouter';

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <AppRouter />
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;