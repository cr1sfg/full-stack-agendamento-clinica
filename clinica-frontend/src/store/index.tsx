import { configureStore } from '@reduxjs/toolkit'

import medicosReducers from './reducers/cadastroMedico'

const store = configureStore({
  reducer: {
    medicos: medicosReducers
  }
})

export type RootReducer = ReturnType<typeof store.getState>

export default store