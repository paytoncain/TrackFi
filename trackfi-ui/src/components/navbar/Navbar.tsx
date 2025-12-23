import type {ReactNode} from "react";

export const Navbar = ({title, children}: {title: string, children?: ReactNode}) => {
  return <div className={"navbar shadow-sm inline-flex"}>
    <span className={"text-2xl font-semibold"}>{title}</span>
    <span className={"grow"}/>
    {children}
  </div>
}