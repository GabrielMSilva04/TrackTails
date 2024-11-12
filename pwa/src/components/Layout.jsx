export default function Layout({ children }) {
    return (
        <div>
            <nav>
                <ul>
                <li><a href="/">Home</a></li>
                </ul>
            </nav>
            <div className={"container"}>
                <main>{children}</main>
            </div>
        </div>
    )
}