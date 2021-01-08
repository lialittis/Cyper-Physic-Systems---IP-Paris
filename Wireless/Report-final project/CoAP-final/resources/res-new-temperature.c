/*
 * Copyright (c) 2013, Institute for Pervasive Computing, ETH Zurich
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of the Contiki operating system.
 */

/**
 * \file
 *      Example resource
 * \author
 *      Matthias Kovatsch <kovatsch@inf.ethz.ch>
 */

#include <stdlib.h>
#include <string.h>
#include "rest-engine.h"

#include "extern_var.h"

static void res_get_handler(void *request, void *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

/*
 * A handler function named [resource name]_handler must be implemented for each RESOURCE.
 * A buffer for the response payload is provided through the buffer pointer. Simple resources can ignore
 * preferred_size and offset, but must respect the REST_MAX_CHUNK_SIZE limit for the buffer.
 * If a smaller block size is requested for CoAP, the REST framework automatically splits the data.
 */
RESOURCE(res_new_temperature,
         "title=\"TEMPERATURE\"",
         res_get_handler,
         NULL,
         NULL,
         NULL);

static void
res_get_handler(void *request, void *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
  printf("-----Start to simulate the temperature sensors-----\n");

  const char *len = NULL;
  int temperature_old = temper_info;
  int intern_temperature = temper_info;
  if(temper_info == -1000){
	  intern_temperature = rand() % 6 + 2;
  }else{
	  intern_temperature = temperature_old;
  }
  
  int flag = rand() % 50;
  if(flag % 3 ==0){
	  intern_temperature += 5;
  }else if(flag % 10==0){
	  intern_temperature += 30;
  }else if(flag % 5==0){
	  intern_temperature -= 4;
  }else{
  }

  temper_info = intern_temperature;

  printf("Temperature is captured: %d\n",temper_info);
  
  if(useless ==1 ){
	  printf("No matter what is the temperature, the drug is already useless !\n");
  }else{
	  if(intern_temperature<0){
		  printf("The drug should be useless ! (The alarm should be on)\n");
		  useless = 1;
	  }else if (intern_temperature <2){
		  printf("The temperature is too low ! Heater should be turned on !\n");
	  }else if (intern_temperature >= 2 && intern_temperature <= 8){
		  printf("The temperature is normal !");
	  }else if (intern_temperature >30){
		  printf("The drug should be use less ! (The alarm should be on)\n");
		  useless = 1;
	  }else{
		  printf("The temperature is too high ! Heater should be turned off\n");
	  }
  }

  REST.set_header_content_type(response, REST.type.TEXT_PLAIN); /* text/plain is the default, hence this option could be omitted. */
  // snprintf((char*)buffer, REST_MAX_CHUNK_SIZE,"Alarm is %d",random);
  
  // snprintf((char*)buffer, REST_MAX_CHUNK_SIZE,"Greenhouse temperature is now %d.",intern_temperature);
  snprintf((char*)buffer, REST_MAX_CHUNK_SIZE,"The temperature resource is now %d degree.\n",intern_temperature);
  REST.set_response_payload(response, (int*)buffer, strlen((char*)buffer));
}
