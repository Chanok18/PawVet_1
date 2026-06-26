import Image from "next/image"
import { ArrowUpRight, Stethoscope, Sparkles, Bot, type LucideIcon } from "lucide-react"

type Service = {
  title: string
  description: string
  cta: string
  image: string
  icon: LucideIcon
  tag: string
  overlay: string
  imagePosition?: string
}

const services: Service[] = [
  {
    title: "Citas Médicas",
    description: "Consultas, vacunas y chequeos con veterinarios certificados.",
    cta: "Agendar cita",
    image: "/images/medical.png",
    icon: Stethoscope,
    tag: "Salud",
    overlay:
      "from-[oklch(0.24_0.05_196/0.88)] via-[oklch(0.35_0.06_190/0.58)] to-[oklch(0.45_0.03_190/0.08)]",
    imagePosition: "object-center",
  },
  {
    title: "Servicios Estéticos",
    description: "Baño, peluquería y spa para que luzca radiante y feliz.",
    cta: "Reservar spa",
    image: "/images/grooming.png",
    icon: Sparkles,
    tag: "Bienestar",
    overlay:
      "from-[oklch(0.36_0.11_38/0.82)] via-[oklch(0.46_0.08_50/0.5)] to-[oklch(0.7_0.03_60/0.1)]",
    imagePosition: "object-center",
  },
  {
    title: "Consulta IA",
    description: "Respuestas inmediatas sobre síntomas y cuidados, 24/7.",
    cta: "Preguntar ahora",
    image: "/images/ai-consult.png",
    icon: Bot,
    tag: "Inteligencia",
    overlay:
      "from-[oklch(0.14_0.04_200/0.94)] via-[oklch(0.22_0.07_192/0.7)] to-[oklch(0.34_0.05_190/0.15)]",
    imagePosition: "object-center",
  },
]

export function ServiceCards() {
  return (
    <section className="space-y-4">
      <div className="anim-section flex items-end justify-between">
        <h2 className="font-heading text-[1.5rem] font-semibold text-foreground">
          Nuestros servicios
        </h2>
        <button className="text-sm font-medium text-primary" type="button">
          Ver todo
        </button>
      </div>

      <div className="space-y-4">
        {services.map((service) => {
          const Icon = service.icon
          return (
            <article
              key={service.title}
              className="anim-card group relative h-[16.2rem] overflow-hidden rounded-[2.2rem] shadow-soft transition-transform duration-300 active:scale-[0.985]"
            >
              <Image
                src={service.image || "/placeholder.svg"}
                alt={service.title}
                fill
                sizes="(max-width: 480px) 100vw, 440px"
                className={`object-cover ${service.imagePosition ?? "object-center"} transition-transform duration-700 group-hover:scale-[1.03]`}
              />
              <div
                aria-hidden
                className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(255,255,255,0.35),transparent_35%)]"
              />
              <div
                aria-hidden
                className={`absolute inset-0 bg-gradient-to-r ${service.overlay}`}
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/18 via-transparent to-transparent" />

              <div className="absolute inset-0 flex flex-col justify-between p-6">
                <div className="flex items-center justify-between">
                  <span className="inline-flex w-fit items-center gap-2 rounded-full bg-black/22 px-3.5 py-2 text-[0.98rem] font-semibold text-white backdrop-blur-md">
                    <Icon className="size-3.5" strokeWidth={2} />
                    {service.tag}
                  </span>
                </div>

                <div className="text-white">
                  <h3 className="max-w-[13rem] font-heading text-[clamp(2rem,6vw,2.75rem)] font-semibold leading-[0.95]">
                    {service.title}
                  </h3>
                  <p className="mt-2 max-w-[18rem] text-[1rem] leading-relaxed text-white/90">
                    {service.description}
                  </p>

                  <button
                    type="button"
                    className="mt-5 inline-flex items-center gap-2 rounded-full bg-white px-5 py-3 text-[1.02rem] font-semibold text-foreground shadow-soft transition-transform active:scale-95"
                  >
                    {service.cta}
                    <ArrowUpRight className="size-4" strokeWidth={2.25} />
                  </button>
                </div>
              </div>
            </article>
          )
        })}
      </div>
    </section>
  )
}
