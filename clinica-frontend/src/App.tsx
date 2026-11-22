import React from 'react';
import { Provider } from 'react-redux'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'

import GlobalStyles from './styles/GlobalStyles';

import Home from './pages/Home'
import CadastroMedico from './pages/CadastroMedico';
import CadastroPaciente from './pages/CadastroPaciente'

import store from './store';

const rotas = createBrowserRouter([
  {
    path: '/',
    element: <Home />
  },
  {
    path: '/FormularioMedico',
    element: <CadastroMedico />
  },
  {
    path: '/FormularioPaciente',
    element: <CadastroPaciente />
  }
])


function App() {
  return (
    <Provider store={store}>
    <GlobalStyles />
        <RouterProvider router={rotas} />
    </Provider>
  )
}

export default App;
