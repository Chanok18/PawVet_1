"use client"

import { useState } from "react"
import { Home, CalendarDays, MessageCircle, User } from "lucide-react"

const items = [
  { label: "Inicio", icon: Home },
  { label: "Citas", icon: CalendarDays },
  { label: "Chat IA", icon: MessageCircle },
  { label: "Perfil", icon: User },
]

export function BottomNav() {
  const [active, setActive] = useState(0)

  return (
    <nav className="pointer-events-none fixed inset-x-0 bottom-0 z-50 flex justify-center pb-4">
      <div className="pointer-events-auto mx-auto flex w-[min(92%,420px)] items-center justify-between rounded-[1.75rem] glass border border-border/60 px-2.5 py-2 shadow-float">
        {items.map((item, i) => {
          const Icon = item.icon
          const isActive = i === active
          return (
            <button
              key={item.label}
              type="button"
              onClick={() => setActive(i)}
              aria-current={isActive ? "page" : undefined}
              className={`flex flex-1 flex-col items-center gap-1 rounded-2xl py-2 text-[0.7rem] font-medium transition-colors ${
                isActive ? "text-primary" : "text-muted-foreground"
              }`}
            >
              <span
                className={`grid size-9 place-items-center rounded-xl transition-colors ${
                  isActive ? "bg-primary/12" : "bg-transparent"
                }`}
              >
                <Icon className="size-5" strokeWidth={isActive ? 2.25 : 1.75} />
              </span>
              {item.label}
            </button>
          )
        })}
      </div>
    </nav>
  )
}
