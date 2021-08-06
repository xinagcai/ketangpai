import axios from 'axios'

export function request(config) {
  const request = axios.create()

  return request(config)
}