import {render, screen} from "@testing-library/react";
import {Navbar} from "./Navbar.tsx";
import {expect} from "vitest";
import "@testing-library/jest-dom/vitest"

describe('Navbar', () => {
  it('should render properly', () => {
    render(<Navbar title={"the title"}/>)
    expect(screen.getByText("the title")).toBeInTheDocument()
  })
  it('should have prominent text', () => {
    render(<Navbar title={"the title"}/>)
    expect(screen.getByText("the title")).toHaveClass("text-2xl", "font-semibold")
  })
  it('should render children', () => {
    render(<Navbar title={"the title"}>
      <button>Click Me</button>
    </Navbar>)
    expect(screen.getByText("Click Me")).toBeInTheDocument()
  })
})