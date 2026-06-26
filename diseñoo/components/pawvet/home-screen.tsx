"use client"

import { useRef } from "react"
import { gsap } from "gsap"
import { useGSAP } from "@gsap/react"
import { WelcomeHeader } from "./welcome-header"
import { IntroHero } from "./intro-hero"
import { ServiceCards } from "./service-cards"
import { WellnessStrip } from "./wellness-strip"
import { EmergencySection } from "./emergency-section"
import { BottomNav } from "./bottom-nav"

export function HomeScreen() {
  const root = useRef<HTMLDivElement>(null)

  useGSAP(
    () => {
      const ease = "power3.out"

      gsap.set(
        [".anim-header", ".anim-hero", ".anim-section", ".anim-card"],
        { opacity: 0, y: 24 },
      )

      const tl = gsap.timeline({ defaults: { ease, duration: 0.7 } })

      tl.to(".anim-header", { opacity: 1, y: 0 })
        .to(".anim-hero", { opacity: 1, y: 0, stagger: 0.12 }, "-=0.45")
        .to(
          ".anim-card",
          { opacity: 1, y: 0, stagger: 0.12, duration: 0.6 },
          "-=0.3",
        )
        .to(".anim-section", { opacity: 1, y: 0, stagger: 0.15 }, "-=0.5")
    },
    { scope: root },
  )

  return (
    <div className="relative min-h-dvh overflow-hidden">
      {/* Ambient premium background */}
      <div
        aria-hidden
        className="pointer-events-none fixed inset-0 -z-10"
        style={{
          background:
            "radial-gradient(120% 80% at 100% 0%, oklch(0.92 0.05 186 / 0.55), transparent 55%), radial-gradient(90% 70% at 0% 100%, oklch(0.93 0.05 40 / 0.4), transparent 50%)",
        }}
      />

      <div
        ref={root}
        className="mx-auto w-full max-w-[460px] px-5 pb-28 pt-6"
      >
        <WelcomeHeader />

        <div className="mt-7 space-y-8">
          <IntroHero />
          <WellnessStrip />
          <ServiceCards />
          <EmergencySection />
        </div>
      </div>

      <BottomNav />
    </div>
  )
}
