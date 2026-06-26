import { Heart, Syringe, Weight } from "lucide-react"

const metrics = [
  { label: "Salud general", value: "Excelente", icon: Heart, accent: "text-emerald" },
  { label: "Próxima vacuna", value: "12 días", icon: Syringe, accent: "text-teal" },
  { label: "Peso ideal", value: "8.2 kg", icon: Weight, accent: "text-coral" },
]

export function WellnessStrip() {
  return (
    <section className="anim-section grid grid-cols-3 gap-3">
      {metrics.map((m) => {
        const Icon = m.icon
        return (
          <div
            key={m.label}
            className="rounded-2xl border border-border/60 bg-card p-3.5 shadow-soft"
          >
            <Icon className={`size-5 ${m.accent}`} strokeWidth={1.75} />
            <p className="mt-3 text-sm font-semibold leading-tight text-foreground">
              {m.value}
            </p>
            <p className="mt-0.5 text-[0.7rem] leading-tight text-muted-foreground">
              {m.label}
            </p>
          </div>
        )
      })}
    </section>
  )
}
