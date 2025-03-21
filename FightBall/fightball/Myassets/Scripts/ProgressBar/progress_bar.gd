class_name Healthbar
extends ProgressBar
# Actualiza la barra de salud al inicio

func update_health_bar(actual:float):
	self.value = actual
	
