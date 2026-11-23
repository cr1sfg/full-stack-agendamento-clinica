import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import Paciente from '../../models/Paciente'

const pacienteslice = createSlice({
  name: 'pacientes',
  initialState: {
    itens: [] as Paciente[]
  },
  reducers: {
    remover: (state, action: PayloadAction<number>) => {
      state.itens = state.itens.filter(
        (paciente) => paciente.cpf !== action.payload
      )
    },
    cadastrar: (state, action: PayloadAction<Paciente>) => {
      const pacienteJaExiste = state.itens.find(
        (paciente) => paciente.cpf === action.payload.cpf
      )

      if (pacienteJaExiste) {
        alert('Paciente já cadastrado')
      } else {
        state.itens.push(action.payload)
      }
    },
    editar: (state, action: PayloadAction<Paciente>) => {
      const pacienteJaExiste = state.itens.find(
        (paciente) =>
          paciente.cpf === action.payload.cpf &&
          paciente.cpf !== action.payload.cpf
      )
      if (pacienteJaExiste) {
        alert('Paciente já cadastrado')
        return
      }
      const idx = state.itens.findIndex((c) => c.cpf === action.payload.cpf)
      if (idx !== -1) {
        state.itens[idx] = action.payload
      }
    }
  }
})

export const { remover, cadastrar, editar } = pacienteslice.actions

export default pacienteslice.reducer