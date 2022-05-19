export default ({ context }, inject) => {
  let offset = null

  function isDST(date) {
    const jan = new Date(date.getFullYear(), 0, 1).getTimezoneOffset()
    const jul = new Date(date.getFullYear(), 6, 1).getTimezoneOffset()
    return Math.max(jan, jul) !== date.getTimezoneOffset()
  }

  function getTimezoneOffset() {
    if (offset === null) {
      const date = new Date()
      let minutes = date.getTimezoneOffset()*-1
      if (isDST(date)) minutes -= 60
      offset = minutes/60
    }
    return offset
  }

  inject('timezoneOffset', getTimezoneOffset)
}
