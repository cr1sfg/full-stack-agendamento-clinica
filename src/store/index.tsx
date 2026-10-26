import { configureStore } from '@reduxjs/toolkit'
import pacientesReducer from './reducers/cadastroPaciente'

const store = configureStore({
	reducer: {
		pacientes: pacientesReducer
	}
})

export type RootReducer = ReturnType<typeof store.getState>

export default store