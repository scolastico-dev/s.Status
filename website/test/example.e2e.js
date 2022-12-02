import {expect, $} from '@wdio/globals'
import {render, screen} from '@testing-library/vue'

import * as matchers from '@testing-library/jest-dom/matchers'
expect.extend(matchers)

import HeaderBar from '@/HeaderBar.vue'

describe('vue component tests', () => {
  it('should do something cool', async () => {
    render(Component, {prop: 'foobar'})
    const component = screen.getByText(/Click me!/i)

    const elem = await $(component)
    await elem.click()
    expect(elem).toContainText('Click me!')
  })
})

