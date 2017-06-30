/*
 * The MIT License
 *
 * Copyright 2017 Wikimark.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.wikimark;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author Martín Straus
 */
public class VelocityContextAdapter implements org.apache.velocity.context.Context {
    
    public static class Dates {
        public String format(Instant instant) {
            return DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL)
                .withZone(ZoneId.systemDefault())
                .format(instant);
        }
    }

    private final VelocityContext context;

    public VelocityContextAdapter(PageContext pageContext, Context appContext) {
        context = new VelocityContext();
        context.put("page", pageContext);
        context.put("context", appContext);
        context.put("pageURL", pageContext.url(appContext));
        context.put("dates", new Dates());
    }

    @Override
    public Object put(String string, Object o) {
        return context.put(string, o);
    }

    @Override
    public Object get(String string) {
        return context.get(string);
    }

    @Override
    public boolean containsKey(Object o) {
        return context.containsKey(o);
    }

    @Override
    public Object[] getKeys() {
        return context.getKeys();
    }

    @Override
    public Object remove(Object o) {
        return context.remove(o);
    }

}
