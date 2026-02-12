import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import Medico from '../../models/Medico'

const medicosSlice = createSlice({
  name: 'medicos',
  initialState: {
    itens: [] as Medico[]
  },
  reducers: {
    remover: (state, action: PayloadAction<number>) => {
      state.itens = state.itens.filter(
        (medico) => medico.crm !== action.payload
      )
    },
    cadastrar: (state, action: PayloadAction<Medico>) => {
      const medicoJaExiste = state.itens.find(
        (medico) => medico.crm === action.payload.crm
      )

      if (medicoJaExiste) {
        alert('Médico já cadastrado')
      } else {
        state.itens.push(action.payload)
      }
    },
    editar: (state, action: PayloadAction<Medico>) => {
      const medicoJaExiste = state.itens.find(
        (medico) =>
          medico.crm === action.payload.crm &&
          medico.crm !== action.payload.crm
      )
      if (medicoJaExiste) {
        alert('Médico já cadastrado')
        return
      }
      const idx = state.itens.findIndex((c) => c.crm === action.payload.crm)
      if (idx !== -1) {
        state.itens[idx] = action.payload
      }
    }
  }
})

export const { remover, cadastrar, editar } = medicosSlice.actions

export default medicosSlice.reducer